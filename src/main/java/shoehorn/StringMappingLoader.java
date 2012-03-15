package shoehorn;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Properties;

/**
 * @author Ryan Brainard
 */
class StringMappingLoader extends MappingLoader {

    @Override
    protected Closeable inject(String mapString, Properties properties) throws IOException {
        final Reader mapStringReader = new StringReader(mapString.replaceAll(";", "\n"));
        properties.load(mapStringReader);
        return mapStringReader;
    }
}
