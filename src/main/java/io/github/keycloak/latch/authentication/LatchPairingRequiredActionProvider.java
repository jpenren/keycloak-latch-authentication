package io.github.keycloak.latch.authentication;

import javax.ws.rs.core.Response;

import org.keycloak.authentication.RequiredActionContext;
import org.keycloak.authentication.RequiredActionProvider;

public class LatchPairingRequiredActionProvider implements RequiredActionProvider {

    public void close() {
        // 
    }

    public void evaluateTriggers(RequiredActionContext context) {
        
    }

    public void requiredActionChallenge(RequiredActionContext context) {
        Response challenge = context.form().createForm("latch-pairing.ftl");
        context.challenge(challenge);
    }

    public void processAction(RequiredActionContext context) {
        String answer = (context.getHttpRequest().getDecodedFormParameters().getFirst("secret_answer"));
//        UserCredentialModel model = new UserCredentialModel();
//        model.setValue(answer);
//        model.setType("latch");
        context.getUser().setSingleAttribute("latch", answer);
        context.success();
    }

}
