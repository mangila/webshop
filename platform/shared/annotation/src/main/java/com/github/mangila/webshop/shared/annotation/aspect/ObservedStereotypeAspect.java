package com.github.mangila.webshop.shared.annotation.aspect;

import com.github.mangila.webshop.shared.annotation.ObservedStereotype;
import io.micrometer.common.KeyValue;
import io.micrometer.common.KeyValues;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;

import java.util.*;

@Aspect
@Component
public class ObservedStereotypeAspect {

    private final ObservationRegistry registry;
    private final ObservedStereotypePostProcessor processor;

    public ObservedStereotypeAspect(ObservationRegistry registry,
                                    ObservedStereotypePostProcessor processor) {
        this.registry = registry;
        this.processor = processor;
    }

    @Around(value = "@within(com.github.mangila.webshop.shared.annotation.ObservedService)" +
                    " || @within(com.github.mangila.webshop.shared.annotation.ObservedRepository)" +
                    " || @within(com.github.mangila.webshop.shared.annotation.ObservedComponent)")
    public Object observeAnnotation(ProceedingJoinPoint pjp) throws Throwable {
        Class<?> targetClass = pjp.getTarget().getClass();
        var annotationData = processor.observedBeans.get(targetClass);
        var observation = Observation.start(annotationData.name, registry)
                .lowCardinalityKeyValues(annotationData.tags)
                .lowCardinalityKeyValue("method", pjp.getSignature().getName())
                .highCardinalityKeyValue("payload", Arrays.toString(pjp.getArgs()))
                .contextualName(targetClass.getSimpleName().concat("#").concat(pjp.getSignature().getName()));
        try (Observation.Scope scope = observation.openScope()) {
            observation.event(Observation.Event.of("Method call", "Method Start"));
            return pjp.proceed();
        } catch (Exception e) {
            observation.error(e);
            observation.stop();
            throw e;
        } finally {
            observation.stop();
            observation.event(Observation.Event.of("Method call", "Method End"));
        }
    }

    @Component
    public static class ObservedStereotypePostProcessor implements BeanPostProcessor {

        private final Map<Class<?>, AnnotationData> observedBeans = new HashMap<>();

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) {
            ObservedStereotype observedStereotype = AnnotatedElementUtils.findMergedAnnotation(bean.getClass(), ObservedStereotype.class);
            if (Objects.nonNull(observedStereotype)) {
                String name = observedStereotype.name().isEmpty() ? beanName : observedStereotype.name();
                String[] tags = observedStereotype.tags();
                Class<?> clazz = AopUtils.getTargetClass(bean);
                var metadata = new AnnotationData(name, KeyValues.of(
                                KeyValue.of("bean", beanName),
                                KeyValue.of("class", clazz.getSimpleName()))
                        .and(createTags(tags)));
                observedBeans.put(clazz, metadata);
            }
            return bean;
        }

        private record AnnotationData(String name, KeyValues tags) {
        }

        private KeyValues createTags(String[] tags) {
            if (tags.length % 2 != 0) {
                throw new IllegalArgumentException("tags Array must be in key value pairs");
            }
            var keyvalues = new ArrayList<KeyValue>();
            for (int i = 0; i < tags.length - 1; i += 2) {
                var key = tags[i];
                var value = tags[i + 1];
                keyvalues.add(KeyValue.of(key, value));
            }
            return keyvalues.isEmpty() ? KeyValues.empty() : KeyValues.of(keyvalues.toArray(KeyValue[]::new));
        }
    }
}