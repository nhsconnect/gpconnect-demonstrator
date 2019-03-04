/*
 Copyright 2019  Simon Farrow <simon.farrow1@hscic.gov.uk>

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package uk.gov.hscic.common.validators;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import static uk.gov.hscic.appointments.AppointmentResourceProvider.throwInvalidResource;

/**
 * ValidatorChecker (VC)
 * The whole point of this is for brevity hence the short name
 * executes test and throws exception with string
 * @author simonfarrow
 */
public class VC {

    private final BooleanSupplier test;     // lambda taking no parameters and returning a Boolean
    private final Supplier<String> message; // lambda taking no parameters and returning a String
    
    /**
     * 
     * @param test lambda function to evaluate
     * @param message lambda function returning text message in exception
     */
    public VC(BooleanSupplier test, Supplier<String> message) {
        this.test = test;
        this.message = message;
    }
    
    /**
     * performs the validation check
     * executes the test and throws and  throws an InvalidResource exception
     * using the derived message
     */
    public void execute() {
        if (test.getAsBoolean()) {
            throwInvalidResource(message.get());
        }
    }
    
    /**
     * static method to perform validation checks 
     * @param vcs Array of ValidationCheck objects
     */
    public static void execute(VC[] vcs) {
        for (VC vc : vcs) {
            vc.execute();
        }
    }
}
