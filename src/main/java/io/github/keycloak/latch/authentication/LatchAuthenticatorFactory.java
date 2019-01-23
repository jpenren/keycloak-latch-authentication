package io.github.keycloak.latch.authentication;

import java.util.List;

import org.keycloak.Config.Scope;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel.Requirement;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;

public class LatchAuthenticatorFactory implements AuthenticatorFactory {
    public static final String PROVIDER_ID = "latch-authenticator";
    private static final LatchAuthenticator SINGLETON = new LatchAuthenticator();

    public void close() {
        // Nothing to do
    }

    public Authenticator create(KeycloakSession session) {
        return SINGLETON;
    }

    public String getId() {
        return PROVIDER_ID;
    }

    public void init(Scope scope) {
        // Nothing to do
    }

    public void postInit(KeycloakSessionFactory factory) {
        // Nothing to do
    }

    public String getDisplayType() {
        return "Latch Authenticator";
    }

    public String getReferenceCategory() {
        return UserCredentialModel.TOTP;
    }

    public Requirement[] getRequirementChoices() {
        return new Requirement[] {Requirement.ALTERNATIVE, Requirement.REQUIRED};
    }

    public boolean isConfigurable() {
        return true;
    }

    public boolean isUserSetupAllowed() {
        return true;
    }

    public List<ProviderConfigProperty> getConfigProperties() {
        
        return ProviderConfigurationBuilder.create()
                .property()
                .name("key")
                .label("key")
                .add()
                .build();
    }

    public String getHelpText() {
        return "Latch Authenticator";
    }

}
