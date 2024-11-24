package io.github.nichetoolkit.file;

import io.github.nichetoolkit.rest.RestException;

import java.io.InputStream;
import java.util.Collection;

public interface StoreService {

    InputStream read(String id) throws RestException;

    void write(String id, InputStream inputStream) throws RestException;

    void marge(String id, Collection<String> idList) throws RestException;

    void delete(String id) throws RestException;

    void delete(Collection<String> idList) throws RestException;

    void rename(String id, String rename) throws RestException;

}
