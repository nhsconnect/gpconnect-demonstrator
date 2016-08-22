'use strict';

angular.module('gpConnect')
        .factory('fhirJWTFactory', function () {

            var getJWT = function (scope, operation, identifier) {

                identifier = '' + identifier; // Force value to be a string as it does not alway come through as one

                // Header
                var oHeader = {alg: 'none', typ: 'JWT'};

                // Payload
                var oPayload = {};
                var tNow = KJUR.jws.IntDate.get('now');
                var endTime = KJUR.jws.IntDate.get('now') + 300000;
                endTime = '' + endTime; // Convert to string
                var tEnd = KJUR.jws.IntDate.get(endTime); //Add 5 minutes, the library does not have a default mthod dor adding minutes
                oPayload.iss = window.location.href;
                oPayload.sub = "1"; // Dummy user as we don't have a login so all users are the same
                oPayload.aud = "https://authorize.fhir.nhs.net/token";
                oPayload.exp = tEnd;
                oPayload.iat = tNow;
                oPayload.reason_for_request = "directcare";

                if (scope.indexOf("patient") > -1) {
                    oPayload.requested_record = {
                        "resourceType": "Patient",
                        "identifier": [{
                                "system": "http://fhir.nhs.net/Id/nhs-number",
                                "value": identifier
                            }]
                    };
                    oPayload.requested_scope = "patient/*." + operation;
                } else {
                    oPayload.requested_record = {
                        "resourceType": "Organization",
                        "identifier": [{
                                "system": "http://fhir.nhs.net/Id/ods-organization-code",
                                "value": identifier
                            }]
                    };
                    oPayload.requested_scope = "organization/*." + operation;
                }

                oPayload.requesting_device = {
                    "resourceType": "Device",
                    "id": "1",
                    "identifier": [{
                            "system": "Web Interface",
                            "value": "GP Connect Demonstrator"
                        }],
                    "model": "Demonstrator",
                    "version": "1.0"
                };
                oPayload.requesting_organization = {
                    "resourceType": "Organization",
                    "id": "1",
                    "identifier": [{
                            "system": "http://fhir.nhs.net/Id/ods-organization-code",
                            "value": "[ODSCode]"
                        }],
                    "name": "GP Connect Demonstrator"
                };
                oPayload.requesting_practitioner = {
                    "resourceType": "Practitioner",
                    "id": "1",
                    "identifier": [{
                            "system": "http://fhir.nhs.net/sds-user-id",
                            "value": "G13579135"
                        },
                        {
                            "system": "localSystem",
                            "value": "1"
                        }],
                    "name": {
                        "family": ["Demonstrator"],
                        "given": ["GPConnect"],
                        "prefix": ["Mr"]
                    }
                };

                // Sign JWT, password=123456
                var sHeader = JSON.stringify(oHeader);
                var sPayload = JSON.stringify(oPayload);
                var sJWT = KJUR.jws.JWS.sign("none", sHeader, sPayload, "");
                return sJWT;
            };

            var guid = function() {
                function s4() {
                    return Math.floor((1 + Math.random()) * 0x10000)
                            .toString(16)
                            .substring(1);
                }
                return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
                        s4() + '-' + s4() + s4() + s4();
            }

            return {
                getJWT: getJWT,
                guid: guid
            };
        });

