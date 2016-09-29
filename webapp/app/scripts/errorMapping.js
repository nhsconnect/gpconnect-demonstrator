var errorMapping = function (httpStatus, httpStatusText) {
    switch (httpStatus) {
        case 403:
            if(httpStatusText == "NO_PATIENT_CONSENT"){
                return "No Patient Consent To Share, patient has not given consent.";
            } else if(httpStatusText == "NO_ORGANISATIONAL_CONSENT"){
                return "No Organisational Consent To Share, data sharing consent has not been given.";
            } else if(httpStatusText == "NON_AUTHORITATIVE"){
                return "Non Authoritative, data can not be shared.";
            } else {
                return "Data Authorization Error, data can not be shared.";
            }
        default:
            return undefined;
    }
};