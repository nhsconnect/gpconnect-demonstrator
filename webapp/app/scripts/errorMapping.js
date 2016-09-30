var errorMapping = function (httpStatus) {
    switch (httpStatus) {
        case 403:
            return "Data Authorization Error.";
        case 429:
            return "Too many requests are being made to the recieving system and no response is available.";
        case 495:
            return "SSL Certificate Error, the certificate used for the request is invalid.";
        case 496:
            return "SSL Certificate is required and was not supplied with the request.";
        case 503:
            return "Service Unavailable, cannot retrieve data.";
        default:
            return undefined;
    }
};