package org.google.home.config.factory;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.MERGE)
@Sources({ "system:env" })
public interface IFrameworkConfig extends Config {

	@Key("HOMEPAGE_VALIDATION")
	String homePageValidation();

	@Key("ENVIRONEMNT")
	String environemnt();

	@Key("APPURL")
	String appURL();

	@Key("BROWSER")
	String browser();

	@Key("IMPLICIT_WAIT")
	int implicitTimeOut();

	@Key("EXPLICIT_TIMEOUT")
	int explicittimeout();	

	@Key("EMAILENDPOINT")
	String emailEndpoint();

	@Key("EMAILSUBJECT")
	String emailSubject();

	@Key("EMAILBODY")
	String emailBody();

	@Key("EMAILISHTML")
	String emailIsHTML();

	@Key("EMAILTOLIST")
	String toEmailList();

	@Key("EMAILCCLIST")
	String ccEmailList();

	@Key("EMAILSERVICEKEY")
	String emailServiceKey();

	@Key("TESTREPORTEMAIL")
	String emailTestReport();

	@Key("TESTREPORTPDF")
	String pdfTestReport();
	
	@Key("DEFAULT_DOWNLOAD_DIRECTRY")
	String defaultDownloadingDirectory();
	
}
