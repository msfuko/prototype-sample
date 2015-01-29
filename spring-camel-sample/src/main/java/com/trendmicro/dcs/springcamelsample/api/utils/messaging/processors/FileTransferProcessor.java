package com.trendmicro.dcs.springcamelsample.api.utils.messaging.processors;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.annotation.Resource;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.trendmicro.dcs.springcamelsample.api.entity.FileMessage;

/**
 * Using to check the source file is in the fileDierctory.
 * Only the files in fileDierctory will go through file route to S3.
 *
 */
@Component
public class FileTransferProcessor implements Processor {

	@Resource(name = "fileDirectory")
	private String fileDirectory;

	@Resource(name = "fileEndpoint")
	private String fileEndpoint;
	
	private void createDoneFile(File dest) throws FileNotFoundException {
		System.out.println(dest + ".done");
		FileOutputStream done = new FileOutputStream(dest + ".done");
		PrintStream print = new PrintStream(done);
		print.println("done");
		print.flush();
		print.close();
	}
	
	@Override
	public void process(Exchange exchange) throws Exception {
		FileMessage message = (FileMessage) exchange.getIn().getBody();
		File source = new File(((FileMessage)message).getFilePath());
		File dest = new File(FilenameUtils.concat(fileDirectory, source.getName()));

		// if src file is not in dest folder, move it to dest for routing
		if (!StringUtils.equals(source.getAbsolutePath(), dest.getAbsolutePath())) {
			System.out.println("copy file - source " + source + " ; dest ; " + dest);
			FileUtils.copyFile(source, dest);
		}
		// create .done file for notifying routing
		this.createDoneFile(dest);
	}

}
