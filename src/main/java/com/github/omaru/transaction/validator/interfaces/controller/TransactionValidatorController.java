package com.github.omaru.transaction.validator.interfaces.controller;

import com.github.omaru.transaction.validator.application.mapper.ReportMapper;
import com.github.omaru.transaction.validator.application.response.ReportResponse;
import com.github.omaru.transaction.validator.application.usecase.ValidateCsvRecords;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/v1/transactions")
@RequiredArgsConstructor
public class TransactionValidatorController {
    private final ValidateCsvRecords validateCsvRecords;
    private final ReportMapper reportMapper;

    @Operation(description = "Validate Csv Transactions (JSON)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report created", content =
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ReportResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReportResponse> validateTransactionsJson(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(reportMapper.toResponse(validateCsvRecords.execute(file)));
    }

    @Operation(description = "Validate Csv Transactions (HTML)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report created",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.TEXT_HTML_VALUE)
    public String validateTransactionsHtml(@RequestParam("file") MultipartFile file, Model model) {
        var report = reportMapper.toResponse(validateCsvRecords.execute(file));
        model.addAttribute("report", report);
        return "report";
    }
}
