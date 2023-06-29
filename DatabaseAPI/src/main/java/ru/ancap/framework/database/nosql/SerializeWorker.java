package ru.ancap.framework.database.nosql;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.util.zip.DataFormatException;

public interface SerializeWorker<T> {

    SerializeWorker<String> STRING = new AbstractSerializeWorker<>(String.class) {
        @Override public String serialize(String object) { return object; }
        @Override public String deserialize(String object) { return object; }
    };

    SerializeWorker<Long> INTEGER = new AbstractSerializeWorker<>(Long.class) {
        @Override public String serialize(Long object) { return object.toString(); }
        @Override public Long deserialize(String object) { return Long.parseLong(object); }
    };

    SerializeWorker<Double> NUMBER = new AbstractSerializeWorker<>(Double.class) {
        @Override public String serialize(Double object) { return object.toString(); }
        @Override public Double deserialize(String object) { return Double.parseDouble(object); }
    };

    SerializeWorker<Boolean> BOOLEAN = new AbstractSerializeWorker<>(Boolean.class) {
        @Override public String serialize(Boolean object) { return object.toString(); }
        @SneakyThrows
        @Override public Boolean deserialize(String object) {
            if (object.equalsIgnoreCase("false")) return false;
            else if (object.equalsIgnoreCase("true")) return true;
            else throw new DataFormatException();
        }
    };

    Class<?> dataType();
    String serialize(T object);
    T deserialize(String object);

    @AllArgsConstructor
    abstract class AbstractSerializeWorker<T> implements SerializeWorker<T> {

        private final Class<?> dataType;

        public Class<?> dataType() { return this.dataType; }

    }

}