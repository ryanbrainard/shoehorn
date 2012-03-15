package shoehorn;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Ryan Brainard
 */
class FileMappingLoader extends MappingLoader {

    @Override
    protected Closeable inject(String mapFile, Properties properties) throws IOException {
        final InputStream mapFileStream = new FileInputStream(mapFile);
        properties.load(mapFileStream);
        return mapFileStream;
    }
}
