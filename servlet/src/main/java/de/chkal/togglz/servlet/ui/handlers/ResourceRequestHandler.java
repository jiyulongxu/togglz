package de.chkal.togglz.servlet.ui.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.chkal.togglz.servlet.ui.RequestHandlerBase;

public class ResourceRequestHandler extends RequestHandlerBase {

    private final Pattern PATTERN = Pattern.compile(".*/(\\w+)\\.(css|js|png)$");

    @Override
    public boolean handles(String path) {
        return PATTERN.matcher(path).matches();
    }

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Matcher matcher = PATTERN.matcher(request.getRequestURI());
        if (matcher.matches()) {

            String basename = matcher.group(1);
            String type = matcher.group(2);

            InputStream stream = loadResource(basename + "." + type);
            if (stream == null) {
                response.sendError(404);
                return;
            }

            if ("css".equals(type)) {
                response.setContentType("text/css");
            } else if ("js".equals(type)) {
                response.setContentType("text/javascript");
            } else if ("png".equals(type)) {
                response.setContentType("image/png");
            }

            copy(stream, response.getOutputStream());

        }

    }

    private void copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
    }

}
