package com.rabobank.customer.statementsprocessor;

import com.rabobank.customer.statementsprocessor.service.CsvStatementsService;
import com.rabobank.customer.statementsprocessor.service.ValidationService;
import com.rabobank.customer.statementsprocessor.service.XmlStatementsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *  This is the Spring unit testing for testing positive and negative scenarios
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(ValidationService.class)
@AutoConfigureMockMvc
public class StatementsApplicationTests {
	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private XmlStatementsService xmlStatementsService;

	@MockBean
	private CsvStatementsService csvStatementsService;

	@Test
	// To test xml processor
	public void testXmlProcessor() throws Exception{
		StringBuilder fileContents = new StringBuilder();
		fileContents.append("<records><record reference=\"187997\">");
		fileContents.append("<accountNumber>NL91RABO0315273637</accountNumber>");
		fileContents.append("    <description>Clothes for Rik King</description>");
		fileContents.append("    <startBalance>100.00</startBalance>");
		fileContents.append("    <mutation>200.00</mutation>");
		fileContents.append("    <endBalance>-100.00</endBalance></records>");
		MockMultipartFile multipartFile = new MockMultipartFile("file", fileContents.toString().getBytes());
		MockHttpServletRequestBuilder builder =
				MockMvcRequestBuilders.fileUpload("/customer/statements/xmlprocessor")
						.file(multipartFile);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();;
		this.mockMvc.perform(builder)
				.andExpect(status().is(200));

	}

	@Test
	// To test csv processor
	public void testCsvProcessor() throws Exception{
		StringBuilder fileContents = new StringBuilder();
		fileContents.append("Reference,Account Number,Description,Start Balance,Mutation,End Balance\n");
		fileContents.append("177666,NL93ABNA0585619023,Flowers for Rik Theuß,44.85,-22.24,22.61\n");
		fileContents.append("112806,NL69ABNA0433647324,Subscription for Jan Theuß,45.59,+48.18,93.77\n");
		fileContents.append("158338,NL91RABO0315273637,Tickets for Vincent King,12.76,-39.5,-26.74\n");
		fileContents.append("193499,NL93ABNA0585619023,Candy for Daniël Dekker,88.44,-13.28,75.16\n");
		fileContents.append("112806,NL90ABNA0585647886,Clothes from Peter de Vries,32.76,+49.03,81.79\n");
		fileContents.append("112806,NL91RABO0315273637,Tickets for Erik Dekker,41.63,+12.41,54.04\n");
		fileContents.append("108230,NL32RABO0195610843,Flowers for Willem Bakker,43.63,-12.18,31.45\n");
		fileContents.append("196213,NL32RABO0195610843,Subscription from Rik de Vries,30.36,-35.1,-4.74\n");
		fileContents.append("109762,NL93ABNA0585619023,Flowers from Rik de Vries,47.45,+17.82,65.27\n");
		fileContents.append("163590,NL27SNSB0917829871,Tickets from Rik Bakker,105.11,+29.87,134.98");

		MockMultipartFile multipartFile = new MockMultipartFile("file", fileContents.toString().getBytes());
		MockHttpServletRequestBuilder builder =
				MockMvcRequestBuilders.fileUpload("/customer/statements/csvprocessor")
						.file(multipartFile);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();;
		this.mockMvc.perform(builder)
				.andExpect(status().is(200));

	}
	@Test
	// To test negative scenarios
	public void testErrorScenarios() throws Exception{
		MockHttpServletRequestBuilder builder =
				MockMvcRequestBuilders.fileUpload("/customer/statements/xmlprocessor");
						//.file(multipartFile);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();;
		this.mockMvc.perform(builder)
				.andExpect(status().is(400));

	}
}
