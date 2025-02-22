package eu.okaeri.configs.serdes.impl;

import eu.okaeri.configs.schema.GenericsPair;
import eu.okaeri.configs.serdes.ObjectTransformer;

public class StringToStringTransformer extends ObjectTransformer<String, String> {

    @Override
    public GenericsPair getPair() {
        return this.genericsPair(String.class, String.class);
    }

    @Override
    public String transform(String data) {
        return data;
    }
}
