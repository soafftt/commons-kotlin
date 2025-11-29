package soft.soft.valkey

import glide.api.GlideClient
import glide.api.commands.BitmapBaseCommands
import glide.api.commands.GenericBaseCommands
import glide.api.commands.GeospatialIndicesBaseCommands
import glide.api.commands.HashBaseCommands
import glide.api.commands.HyperLogLogBaseCommands
import glide.api.commands.ListBaseCommands
import glide.api.commands.PubSubBaseCommands
import glide.api.commands.ScriptingAndFunctionsBaseCommands
import glide.api.commands.SetBaseCommands
import glide.api.commands.SortedSetBaseCommands
import glide.api.commands.StreamBaseCommands
import glide.api.commands.StringBaseCommands
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.DisposableBean

class ValkeyCommands private constructor(
    val bitmapCommands: BitmapBaseCommands,
    val genericCommands: GenericBaseCommands,
    val stringCommands: StringBaseCommands,
    val hashCommands: HashBaseCommands,
    val listCommands: ListBaseCommands,
    val setCommands: SetBaseCommands,
    val sortedSetCommands: SortedSetBaseCommands,
    val streamCommands: StreamBaseCommands,
    val hyperLogLogCommands: HyperLogLogBaseCommands,
    val geospatialIndicesCommands: GeospatialIndicesBaseCommands,
    val scriptingAndFunctionsCommands: ScriptingAndFunctionsBaseCommands,
    val pubSubCommands: PubSubBaseCommands,
    private val glideClient: GlideClient
) : DisposableBean {

    companion object {

        private val logger = LoggerFactory.getLogger(ValkeyCommands::class.java)

        fun from(glideClient: GlideClient): ValkeyCommands {
            return ValkeyCommands(
                glideClient as BitmapBaseCommands,
                glideClient as GenericBaseCommands,
                glideClient as StringBaseCommands,
                glideClient as HashBaseCommands,
                glideClient as ListBaseCommands,
                glideClient as SetBaseCommands,
                glideClient as SortedSetBaseCommands,
                glideClient as StreamBaseCommands,
                glideClient as HyperLogLogBaseCommands,
                glideClient as GeospatialIndicesBaseCommands,
                glideClient as ScriptingAndFunctionsBaseCommands,
                glideClient as PubSubBaseCommands,
                glideClient
            )
        }
    }

    override fun destroy() {
        try {
            glideClient.close()
        } catch (e: Exception) {
            logger.error(e.message, e)
        }
    }
}