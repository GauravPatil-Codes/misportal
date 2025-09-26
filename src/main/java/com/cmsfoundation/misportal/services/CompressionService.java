package com.cmsfoundation.misportal.services;

import java.io.IOException;
import java.io.InputStream;

public interface CompressionService {
InputStream compressImage(InputStream inputStream, int width, int height, float quality) throws IOException;

}
