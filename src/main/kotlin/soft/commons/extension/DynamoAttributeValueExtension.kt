package soft.commons.extension

import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate

class DynamoAttributeValueExtension {

    companion object {
        fun String.dynamoNotExistsPrimaryKey() = "attribute_not_exists(${this})"
        fun String.dynamoExistsPrimaryKey() = "attribute_exists(${this})"

        fun Map<String, AttributeValue>.getStringValue(key: String): String? = when {
            containsKey(key) -> get(key)!!.s()
            else -> null
        }

        fun Map<String, AttributeValue>.getStringValue(key: String, defaultValue: String = ""): String = when {
            containsKey(key) -> get(key)!!.s()
            else -> defaultValue
        }

        fun Map<String, AttributeValue>.getIntValue(key: String): Int? = when {
            containsKey(key) -> get(key)!!.n()!!.toInt()
            else -> null
        }

        fun Map<String, AttributeValue>.getIntValue(key: String, defaultValue: Int = 0): Int = when {
            containsKey(key) -> get(key)!!.n()!!.toInt()
            else -> defaultValue
        }

        fun Map<String, AttributeValue>.getLongValue(key: String): Long? = when {
            containsKey(key) -> get(key)!!.n()!!.toLong()
            else -> null
        }

        fun Map<String, AttributeValue>.getLongValue(key: String, defaultValue: Long = 0L): Long = when {
            containsKey(key) -> get(key)!!.n()!!.toLong()
            else -> defaultValue
        }

        fun Map<String, AttributeValue>.getBooleanValue(key: String): Boolean? = when {
            containsKey(key) -> get(key)!!.bool()
            else -> null
        }

        fun Map<String, AttributeValue>.getBooleanValue(key: String, defaultValue: Boolean = false): Boolean = when {
            containsKey(key) -> get(key)!!.bool()
            else -> defaultValue
        }

        fun Map<String, AttributeValue>.getMapValue(key: String): Map<String, AttributeValue> = when {
            containsKey(key) -> get(key)!!.m()
            else -> mapOf()
        }

        fun Int.toDynamoAttributeValue(): AttributeValue =
            AttributeValue.builder().n(toString()).build()

        fun Int?.toDynamoAttributeValue(): AttributeValue =
            AttributeValue.builder().n(toString()).build()

        fun Long.toDynamoAttributeValue(): AttributeValue =
            AttributeValue.builder().n(toString()).build()

        fun Long?.toDynamoAttributeValue(): AttributeValue =
            AttributeValue.builder().n(toString()).build()

        @JvmName("toDynamoAttributeValueAllowNull")
        fun String?.toDynamoAttributeValue(): AttributeValue =
            AttributeValue.builder().s(this).build()

        fun String.toDynamoAttributeValue(): AttributeValue =
            AttributeValue.builder().s(this).build()

        fun Boolean.toDynamoAttributeValue(): AttributeValue =
            AttributeValue.builder().bool(this).build()

        fun Boolean?.toDynamoAttributeValue(): AttributeValue =
            AttributeValue.builder().bool(this).build()


        fun Map<String, AttributeValue>.toDynamoAttributeValue(): AttributeValue =
            AttributeValue.builder().m(this).build()

        @JvmName("toDynamoAttributeValueAllowNull")
        fun Map<String, AttributeValue>?.toDynamoAttributeValue(): AttributeValue =
            AttributeValue.builder().m(this).build()

        fun Int?.toDynamoAttributeValueUpdate(): AttributeValueUpdate =
            AttributeValueUpdate.builder().value(toDynamoAttributeValue()).build()

        fun Int.toDynamoAttributeValueUpdate(): AttributeValueUpdate =
            AttributeValueUpdate.builder().value(toDynamoAttributeValue()).build()

        fun Long?.toDynamoAttributeValueUpdate(): AttributeValueUpdate =
            AttributeValueUpdate.builder().value(toDynamoAttributeValue()).build()

        fun Long.toDynamoAttributeValueUpdate(): AttributeValueUpdate =
            AttributeValueUpdate.builder().value(toDynamoAttributeValue()).build()

        @JvmName("toDynamoAttributeValueUpdateAllowNull")
        fun String?.toDynamoAttributeValueUpdate(): AttributeValueUpdate =
            AttributeValueUpdate.builder().value(toDynamoAttributeValue()).build()

        fun String.toDynamoAttributeValueUpdate(): AttributeValueUpdate =
            AttributeValueUpdate.builder().value(toDynamoAttributeValue()).build()

        fun Boolean?.toDynamoAttributeValueUpdate(): AttributeValueUpdate =
            AttributeValueUpdate.builder().value(toDynamoAttributeValue()).build()

        fun Boolean.toDynamoAttributeValueUpdate(): AttributeValueUpdate =
            AttributeValueUpdate.builder().value(toDynamoAttributeValue()).build()

        @JvmName("toDynamoAttributeValueUpdateAllowNull")
        fun Map<String, AttributeValue>?.toDynamoAttributeValueUpdate(): AttributeValueUpdate =
            AttributeValueUpdate.builder().value(toDynamoAttributeValue()).build()

        fun Map<String, AttributeValue>.toDynamoAttributeValueUpdate(): AttributeValueUpdate =
            AttributeValueUpdate.builder().value(toDynamoAttributeValue()).build()
    }
}