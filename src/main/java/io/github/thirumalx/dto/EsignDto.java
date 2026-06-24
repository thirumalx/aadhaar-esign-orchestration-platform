package io.github.thirumalx.dto;

public record EsignDto(String applicationId, String signId,
        String firstName, String middleName, String lastName, String dob,
        String location,
        String fileSourcePath, String fileDestinationPath,
        String successUrl, String failureUrl) {

}
