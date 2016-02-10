package com.uber.departure.times.clients.nextbus.parser;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import com.uber.departure.times.AVertxTestClass;

/**
 * @author Danila Ponomarenko
 */
public abstract class AParserTestClass extends AVertxTestClass {
    @NotNull
    protected String load(@NotNull String fileName) throws IOException {
        StringWriter writer = new StringWriter();
        IOUtils.copy(this.getClass().getClassLoader().getResourceAsStream(fileName), writer, "UTF-8");
        return writer.toString();
    }
}
