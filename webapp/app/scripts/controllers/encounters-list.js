'use strict';

angular.module('gpConnect')
        .controller('EncountersListCtrl', function ($scope, $location, $stateParams, $modal, $state, $sce, PatientService, usSpinnerService, Encounter) {

            $scope.currentPage = 1;
            $scope.toDateValue = moment().format('YYYY-MM-DD');
            $scope.fromDateValue = moment().subtract(3, 'years').format('YYYY-MM-DD');

            $scope.pageChangeHandler = function (newPage) {
                $scope.currentPage = newPage;
            };
            
            if ($stateParams.page) {
                $scope.currentPage = $stateParams.page;
            }

            $scope.openDatePicker = function ($event, name) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.toDate = false;
                $scope.fromDate = false;
                $scope[name] = true;
            };

           $scope.dateChanged = function() {
                
                var toDates = moment($scope.toDateValue);
                if(toDates.isValid())
                {
                    $scope.toDateValue = toDates.format('YYYY-MM-DD');
                 }
        
               var fromDates = moment($scope.fromDateValue);
               if(fromDates.isValid())
               {
                   $scope.fromDateValue = fromDates.format('YYYY-MM-DD');
                } 
                loadHTML();
            
            };

            var loadHTML = function () {
                Encounter.findAllHTMLTables($stateParams.patientId, $scope.fromDateValue || '1970-01-01', $scope.toDateValue|| '1970-01-01').then(function (result) {

                    // Default Page Content
                    var text = '{"provider":"No Data","html":"No encounters data available for this patient."}';
                    $scope.encounterTable = JSON.parse(text);
                    $scope.encounterTable.html = $sce.trustAsHtml($scope.encounterTable.html);

                    // Process Returned Data
                    var fhirJSON = result.data;
                    var entryObj = fhirJSON.entry;
                    $.each(entryObj, function (key, value) {
                        if (value.resource.resourceType == "Patient") { // Find Patient Entry
                            //alert(value.resource.identifier[0].value);
                        }
                        if (value.resource.resourceType == "Composition" && value.resource.type.coding[0].code == "425173008") { // Find Care Record Entry
                            // Check if the requested section exists, if it does not in the back end it will not be passed to the front end
                            if (value.resource.section != undefined) {
                                // We are only going to ever request one setion within the care record entry, this will be the section for displaying on the page 
                                // so we can assume the first section is the one we want to display.
                                $scope.encounterTable.html = $sce.trustAsHtml(value.resource.section[0].text.div);
                                $scope.encounterTable.provider = value.resource.section[0].code.text;
                            }
                        }
                    });

                    usSpinnerService.stop('encounterSummary-spinner');
                });
            };

            // Load the html on page load
            loadHTML();
        });
