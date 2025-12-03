package com.github.omaru.transaction.validator.interfaces.controller;

import com.github.omaru.transaction.validator.application.mapper.ReportMapper;
import com.github.omaru.transaction.validator.application.port.RecordEntryReader;
import com.github.omaru.transaction.validator.application.response.ReportResponse;
import com.github.omaru.transaction.validator.application.usecase.GenerateRecordReport;
import com.github.omaru.transaction.validator.infrastructure.json.RecordEntryJson;
import com.github.omaru.transaction.validator.infrastructure.json.RecordEntryJsonMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/v1/transactions")
@RequiredArgsConstructor
public class TransactionValidatorController {
    private final GenerateRecordReport generateRecordReport;
    private final ReportMapper reportMapper;
    private final RecordEntryReader<MultipartFile> multiPartReader;
    private final RecordEntryJsonMapper jsonMapper;

    @Operation(summary = "Validate transactions")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = RecordEntryJson.class)))})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Report created", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ReportResponse.class))}), @ApiResponse(responseCode = "400", description = "Bad request"), @ApiResponse(responseCode = "500", description = "Something went wrong")})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReportResponse> reportFromJsonToJson(@RequestBody List<RecordEntryJson> recordEntries) {
        return ResponseEntity.ok(reportMapper.toResponse(generateRecordReport.execute(recordEntries.stream().map(jsonMapper::toDomain))));
    }

    @Operation(summary = "Validate transactions ")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = RecordEntryJson.class)))})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Report created", content = {@Content(mediaType = MediaType.TEXT_HTML_VALUE, examples = @ExampleObject(name = "HTML Report Example",
            value = "<html>report</html>"))}), @ApiResponse(responseCode = "400", description = "Bad request"), @ApiResponse(responseCode = "500", description = "Something went wrong")})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String reportFromJsonToHtml(@RequestBody List<RecordEntryJson> recordEntries, Model model) {
        var report = reportMapper.toResponse(generateRecordReport.execute(recordEntries.stream().map(jsonMapper::toDomain)));
        model.addAttribute("report", report);
        return "report";
    }

    @Operation(summary = "Validate transactions")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {@Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)})
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "Report created",
            content = {@Content(mediaType = MediaType.TEXT_HTML_VALUE)}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")})
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.TEXT_HTML_VALUE, path = "csv")
    public String reportFromMultiPartToHtml(@Schema @RequestParam("file") MultipartFile file, Model model) {
        var report = reportMapper.toResponse(generateRecordReport.execute(multiPartReader.read(file)));
        model.addAttribute("report", report);
        return "report";
    }

    @Operation(summary = "Validate transactions")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Report created", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ReportResponse.class))}), @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")})
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, path = "csv")
    public ResponseEntity<ReportResponse> reportFromMultiPartToJson(@RequestParam("file") MultipartFile file) {
        var report = reportMapper.toResponse(generateRecordReport.execute(multiPartReader.read(file)));
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(report);
    }
}

