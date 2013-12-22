package com.itrustcambodia.plugin.query.rest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.itrustcambodia.pluggable.core.AbstractWebApplication;
import com.itrustcambodia.pluggable.doc.ApiMethod;
import com.itrustcambodia.pluggable.rest.Controller;
import com.itrustcambodia.pluggable.rest.RequestMapping;
import com.itrustcambodia.pluggable.rest.RequestMethod;
import com.itrustcambodia.pluggable.rest.Result;
import com.itrustcambodia.pluggable.wicket.authroles.Role;
import com.itrustcambodia.pluggable.wicket.authroles.Secured;
import com.itrustcambodia.plugin.query.model.Field;
import com.itrustcambodia.plugin.query.model.Table;

@Controller
public class ImportRestAPI {

    // private static final Logger LOGGER = LoggerFactory
    // .getLogger(ImportRestAPI.class);

    @Secured(roles = { @Role(name = "ROLE_REST_QUERY_PLUGIN_IMPORT", description = "Access Query Plugin Rest Import") })
    @RequestMapping(value = "/queryplugin/api/import", method = RequestMethod.POST)
    @ApiMethod(description = "import data", requestObject = Table.class, responseObject = Void.class)
    public Result importResult(AbstractWebApplication application,
            HttpServletRequest request, HttpServletResponse response)
            throws JsonIOException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        Gson gson = application.getGson();

        InputStreamReader streamReader = new InputStreamReader(
                request.getInputStream());

        Table table = gson.fromJson(streamReader, Table.class);
        if (table == null || table.getName() == null
                || "".equals(table.getName()) || table.getFields() == null
                || table.getFields().length == 0) {
            return Result.badRequest("application/json");
        }
        SimpleJdbcInsert insert = new SimpleJdbcInsert(
                application.getJdbcTemplate());
        insert.withTableName(table.getName());
        Map<String, Object> fields = new HashMap<String, Object>();
        for (Field field : table.getFields()) {
            fields.put(field.getName(), field.getValue());
        }
        try {
            insert.execute(fields);
        } catch (DuplicateKeyException duplicateKeyException) {
            // LOGGER.info(duplicateKeyException.getMessage());
        }
        return Result.ok("application/json");
    }

    @Secured(roles = { @Role(name = "ROLE_REST_QUERY_PLUGIN_IMPORT_BATCH", description = "Access Query Plugin Rest Import Batch") })
    @RequestMapping(value = "/queryplugin/api/import/batch", method = RequestMethod.POST)
    @ApiMethod(description = "import data", requestObject = Table[].class, responseObject = Void.class)
    public Result importBatch(AbstractWebApplication application,
            HttpServletRequest request, HttpServletResponse response)
            throws JsonIOException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        Gson gson = application.getGson();

        InputStreamReader streamReader = new InputStreamReader(
                request.getInputStream());

        Table[] tables = gson.fromJson(streamReader, Table[].class);
        if (tables == null || tables.length == 0) {
            return Result.badRequest("application/json");
        } else {
            for (Table table : tables) {
                if (table == null || table.getName() == null
                        || "".equals(table.getName())
                        || table.getFields() == null
                        || table.getFields().length == 0) {
                    return Result.badRequest("application/json");
                }
            }
        }

        for (Table table : tables) {
            SimpleJdbcInsert insert = new SimpleJdbcInsert(
                    application.getJdbcTemplate());
            insert.withTableName(table.getName());
            Map<String, Object> fields = new HashMap<String, Object>();
            for (Field field : table.getFields()) {
                fields.put(field.getName(), field.getValue());
            }
            try {
                insert.execute(fields);
            } catch (DuplicateKeyException duplicateKeyException) {
                // LOGGER.info(duplicateKeyException.getMessage());
            }
        }

        return Result.ok("application/json");
    }

}
