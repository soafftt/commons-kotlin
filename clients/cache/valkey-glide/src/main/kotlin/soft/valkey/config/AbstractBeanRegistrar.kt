package soft.soft.valkey.config

import org.springframework.beans.factory.BeanRegistrar
import org.springframework.boot.context.properties.bind.Bindable
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.core.env.Environment

abstract class AbstractBeanRegistrar : BeanRegistrar {
    protected var binder: Binder? = null

    protected inline fun <reified T: Any> bindProperties(key: String, env: Environment): T {
        if (this.binder == null) {
            binder = Binder.get(env)
        }

        return binder!!.bind(
            key,
            Bindable.of(T::class.java),
        ).orElseThrow { RuntimeException("Bean registry not found") }
    }
}