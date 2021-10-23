package com.emmanuelc.plagiarismchecker.validation;

import com.emmanuelc.plagiarismchecker.domain.models.enumerations.MimeType;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidFileTypeImpl implements ConstraintValidator<ValidFileType, MultipartFile> {
    @Override
    public boolean isValid(final MultipartFile value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        final Tika tika = new Tika();
        final String mimeTypeValue;
        try {
            mimeTypeValue = tika.detect(value.getBytes());
            final MimeType mime = MimeType.getEnumValue(mimeTypeValue);
            if (mime.equals(MimeType.TEXT_PLAIN)) {
                return true;
            }
        } catch (Exception ignore) {}
        return false;
    }
}
