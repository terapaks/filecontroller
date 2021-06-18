package com.oracle.codekata.service.filecontroller;

import com.oracle.codekata.Driver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.oracle.codekata.data.Data;
import com.oracle.codekata.Driver.eOperation;
import com.oracle.codekata.files.iFileConsumer;
import com.oracle.codekata.files.FileConsumerFactory;
import java.lang.IllegalArgumentException;
import com.oracle.codekata.print.PrintResultsFactory;
import com.oracle.codekata.print.iPrintResults;
import java.lang.IllegalArgumentException;
import java.lang.Exception;
import java.util.ArrayList;


@RestController
@RequestMapping("/files")
public class FileController {

    @GetMapping("/{file}")
    ResponseEntity<String> GetAnalysisFile(@PathVariable("file") String file) throws Exception, IllegalArgumentException{
        try{
            iFileConsumer fc;
            iPrintResults pr;

            ArrayList<Data> dList;
            String val;

            if (file == null) {
                throw new InvalidFileException();
            }
            else {
                eOperation operation = Driver.ValidateCmdLine(file);

                FileConsumerFactory fcFactory = new FileConsumerFactory();
                PrintResultsFactory prFactory = new PrintResultsFactory();

                fc = fcFactory.CreateFileConsumer(operation);
                pr = prFactory.CreatePrintResults(operation);

                if (fc == null || pr == null) {
                    throw new UnhandledException();
                }
            }

            dList = fc.ReadFile();
            pr.GetDetails(dList);
            return ResponseEntity.ok().body(pr.PrintDetails());
        }
        catch (IllegalStateException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch(Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }


}

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="Invalid file specification passed in...")
class InvalidFileException extends RuntimeException {}

@ResponseStatus(reason="Unhandled exception occurred.", value = HttpStatus.EXPECTATION_FAILED)
class UnhandledException extends RuntimeException {}
