package es.gob.signaturereport.configuration.access;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import javax.inject.Qualifier;


	/**
	 * <p>Annotation interface to describe the audit entity manager of the persistence context.</p>
	 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format..</p>
	  * @version 1.0, 07/07/2016.
	 */
	@Qualifier
	@Target({ TYPE, METHOD, PARAMETER, FIELD })
	@Retention(RUNTIME)
	@Documented
	public @interface EVisorConfiguration {

	}


