package org.forpdi.core.abstractions;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação que indica uqe um end point recebe conteúdo
 * multipart data e esse conteúdo deve ser processado
 * pela commons-upload.
 * @author Renato R. R. de Oliveira <renatorro@comp.ufla.br>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ParseMultipartData {

}
