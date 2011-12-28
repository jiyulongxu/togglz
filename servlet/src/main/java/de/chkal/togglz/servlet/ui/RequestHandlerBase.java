package de.chkal.togglz.servlet.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

public abstract class RequestHandlerBase implements RequestHandler {

    private final Charset UTF8 = Charset.forName("UTF8");

    protected void writeResponse(HttpServletResponse response, String body) throws IOException {

        // load the template
        InputStream templateStream = loadResource("template.html");
        BufferedReader templateReader = new BufferedReader(new InputStreamReader(templateStream));

        // prepare the response
        response.setContentType("text/html");
        ServletOutputStream outputStream = response.getOutputStream();

        // write the template to the output stream
        String templateLine = null;
        while ((templateLine = templateReader.readLine()) != null) {
            String outputLine = templateLine.replace("%CONTENT%", body);
            outputStream.write(outputLine.getBytes(UTF8));
        }

        // finished
        response.flushBuffer();

    }

    protected InputStream loadResource(String name) {
        String templateName = RequestHandler.class.getPackage().getName().replace('.', '/') + "/" + name;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResourceAsStream(templateName);
    }

}
