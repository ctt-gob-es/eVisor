package es.gob.signaturereport.persistence.maudit.soap;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;


/** 
 * <p>Class .</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 06/07/2016.
 */
@Qualifier
@Retention(RUNTIME)
@Target({ FIELD, TYPE, METHOD })
public @interface FileSOAPPersistence {
	
}