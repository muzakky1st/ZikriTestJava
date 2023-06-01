package com.exam.ZikriMuzakkyExam.controller;

import com.exam.ZikriMuzakkyExam.entity.JobList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import netscape.javascript.JSObject;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.json.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.logging.Logger;

@RestController
@RequestMapping("/rest/auth")
public class ApplicationController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/job-list")
    public List<Object> getJobList(){
        String uri = "http://dev3.dansmultipro.co.id/api/recruitment/positions.json";
        Object[] objects = restTemplate.getForObject(uri, Object[].class);
        return Arrays.asList(objects);
    }

    @GetMapping("/job-detail/{id}")
    public JobList getDetailJob(@PathVariable("id") String id){
        String uri = "http://dev3.dansmultipro.co.id/api/recruitment/positions/" + id;
        return restTemplate.getForObject(uri, JobList.class);

    }

    @GetMapping(value = "/generate-csv", produces = MediaType.TEXT_PLAIN_VALUE)
        public void generateCsv(HttpServletResponse response) throws  IOException{
        response.setContentType("txt/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"data.csv\"");;
        String uri = "http://dev3.dansmultipro.co.id/api/recruitment/positions.json";

        JSONArray jsonArray = new JSONArray(restTemplate.getForObject(uri, String.class));
        List<String[]> csvData = new ArrayList<>();

        for (int i= 0; i< jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String[] rowData = {
                    jsonObject.getString("id"),
                    jsonObject.getString("type"),
                    jsonObject.getString("url"),
                    jsonObject.getString("created_at"),
                    jsonObject.getString("company"),
                    getCompanyUrl(jsonObject),
                    jsonObject.getString("location"),
                    jsonObject.getString("title"),
                    jsonObject.getString("description")
            };
            csvData.add(rowData);
        }
        try(PrintWriter writer = response.getWriter();
            CSVWriter csvWriter = new CSVWriter(writer)){
            csvWriter.writeAll(csvData);
        }catch (IOException e) {
            throw new IOException("Failed to write CSV data to response", e);
        }
    }

    private String getCompanyUrl(JSONObject jsonObject) {
        if (jsonObject.has("company_url") && !jsonObject.isNull("company_url")) {
            return jsonObject.getString("company_url");
        } else {
            return ""; // Or handle it with a default value or some appropriate action
        }
    }
}
