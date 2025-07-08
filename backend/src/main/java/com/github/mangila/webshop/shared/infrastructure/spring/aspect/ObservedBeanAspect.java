package com.github.mangila.webshop.shared.infrastructure.spring.aspect;

import com.github.mangila.webshop.shared.infrastructure.spring.annotation.ObservedBean;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Aspect
@Component
public class ObservedBeanAspect {

    private final ObservationRegistry registry;
    private final ObservedBeanPostProcessor processor;

    public ObservedBeanAspect(ObservationRegistry registry,
                              ObservedBeanPostProcessor processor) {
        this.registry = registry;
        this.processor = processor;
    }

    @Around(value = "@within(com.github.mangila.webshop.shared.infrastructure.spring.annotation.ObservedService)" +
                    " || @within(com.github.mangila.webshop.shared.infrastructure.spring.annotation.ObservedRepository)")
    public Object observeAnnotation(ProceedingJoinPoint pjp) throws Throwable {
        Class<?> targetClass = pjp.getTarget().getClass();
        var annotationData = processor.observations.get(targetClass);
        var observation = Observation.createNotStarted(annotationData.name, registry)
                .lowCardinalityKeyValues(annotationData.tags)
                .contextualName(targetClass.getSimpleName().concat("#").concat(pjp.getSignature().getName()))
                .start();
        try (Observation.Scope scope = observation.openScope()) {
            return pjp.proceed();
        } catch (Throwable t) {
            observation.error(t);
            throw t;
        } finally {
            observation.stop();
        }
    }

    @Component
    public static class ObservedBeanPostProcessor implements BeanPostProcessor {

        private final Map<Class<?>, AnnotationData> observations = new HashMap<>();

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) {
            ObservedBean observedBean = AnnotatedElementUtils.findMergedAnnotation(bean.getClass(), ObservedBean.class);
            if (Objects.nonNull(observedBean)) {
                String name = observedBean.name().isEmpty() ? beanName : observedBean.name();
                String[] tags = observedBean.tags();
                Class<?> clazz = AopUtils.getTargetClass(bean);
                var metadata = createMetadata(name, tags);
                observations.put(clazz, metadata);
            }
            return bean;
        }

        AnnotationData createMetadata(String name, String[] tags) {
            if (tags.length % 2 != 0) {
                throw new IllegalArgumentException("lowCardinalityKeyValues Array must be in key value pairs");
            }
            var kv = KeyValues.empty();
            for (int i = 0; i < tags.length - 1; i += 2) {
                var key = tags[i];
                var value = tags[i + 1];
                kv.and(KeyValue.of(key, value));
            }
            return new AnnotationData(name, kv);
        }

        record AnnotationData(String name, KeyValues tags) {
        }
    }
}