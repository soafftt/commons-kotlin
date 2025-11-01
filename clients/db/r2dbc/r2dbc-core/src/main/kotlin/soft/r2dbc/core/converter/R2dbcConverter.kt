package soft.r2dbc.core.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter

@ReadingConverter
class LongToBooleanConverter : Converter<Long, Boolean> {
    override fun convert(source: Long): Boolean {
        // 0이면 false, 그 외의 값은 true로 매핑
        return source != 0L
    }
}

@ReadingConverter
class IntegerToBooleanConverter : Converter<Integer, Boolean> {
    override fun convert(source: Integer): Boolean {
        // 0이면 false, 그 외의 값은 true로 매핑
        return !source.equals(0)
    }
}