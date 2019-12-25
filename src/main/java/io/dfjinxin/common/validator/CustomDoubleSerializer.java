package io.dfjinxin.common.validator;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * @Desc:
 * @Author: z.h.c
 * @Date: 2019/12/24 14:14
 * @Version: 1.0
 */
public class CustomDoubleSerializer extends JsonSerializer {
    /**
     * Method that can be called to ask implementation to serialize
     * values of type this serializer handles.
     *
     * @param value       Value to serialize; can <b>not</b> be null.
     * @param gen         Generator used to output resulting Json content
     * @param serializers Provider that can be used to get serializers for
     */
    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (null == value) {
            //write the word 'null' if there's no value available
            gen.writeNull();
        } else {
            final String pattern = ".##";
            //final String pattern = "###,###,##0.00";
            final DecimalFormat myFormatter = new DecimalFormat(pattern);
            final String output = myFormatter.format(value);
            gen.writeNumber(output);
        }
    }
}
