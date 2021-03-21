package eu.okaeri.configs;

import eu.okaeri.configs.schema.ConfigDeclaration;
import eu.okaeri.configs.schema.FieldDeclaration;
import eu.okaeri.configs.schema.GenericsDeclaration;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;


public abstract class OkaeriConfig {

    @Getter @Setter private File bindFile;
    @Getter @Setter private Configurer configurer;
    private ConfigDeclaration declaration;

    public OkaeriConfig() {
        this.declaration = ConfigDeclaration.from(this);
    }

    @SneakyThrows
    public void set(String key, Object value) {
        if (this.configurer == null) {
            throw new IllegalAccessException("configurer cannot be null");
        }
        this.configurer.setValue(key, value);
    }

    @SneakyThrows
    public Object get(String key) {
        if (this.configurer == null) {
            throw new IllegalAccessException("configurer cannot be null");
        }
        return this.configurer.getValue(key);
    }

    @SneakyThrows
    public <T> T get(String key, Class<T> clazz) {
        if (this.configurer == null) {
            throw new IllegalAccessException("configurer cannot be null");
        }
        return this.configurer.getValue(key, clazz, null);
    }

    public void save() throws IllegalAccessException, IOException {

        if (this.bindFile == null) {
            throw new IllegalAccessException("bindFile cannot be null");
        }

        if (this.configurer == null) {
            throw new IllegalAccessException("configurer cannot be null");
        }

        for (FieldDeclaration field : this.declaration.getFields()) {
            this.configurer.setValue(field.getName(), field.getValue());
        }

        this.configurer.writeToFile(this.bindFile, this.declaration);
    }

    public void load() throws IllegalAccessException, IOException {

        if (this.bindFile == null) {
            throw new IllegalAccessException("bindFile cannot be null");
        }

        if (this.configurer == null) {
            throw new IllegalAccessException("configurer cannot be null");
        }

        this.configurer.loadFromFile(this.bindFile, this.declaration);

        for (FieldDeclaration field : this.declaration.getFields()) {
            String fieldName = field.getName();
            if (!this.configurer.keyExists(fieldName)) {
                continue;
            }
            GenericsDeclaration type = field.getType();
            GenericsDeclaration genericType = field.getType();
            Object value = this.configurer.getValue(fieldName, type.getType(), genericType);
            field.updateValue(value);
        }
    }
}
