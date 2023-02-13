package ru.ancap.util.resource;

import java.io.InputStream;

public interface ResourceSource {

    InputStream getResource(String fileName);

}
