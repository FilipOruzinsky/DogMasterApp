import Keycloak from 'keycloak-js'

const keycloak = new Keycloak({
    url: 'http://localhost:8080/',
    realm: 'dog-master-realm', // Change this to your realm name
    clientId: 'dog-master-client', // Change this to your client ID
})

export const initKeycloak = async () => {
    const authenticated = await keycloak.init({
        onLoad: 'login-required',
    })

    if (!authenticated) {
        await keycloak.login()
    }

    keycloak.onTokenExpired = () => {
        keycloak.logout({ redirectUri: window.location.origin })
    }

    return keycloak
}

export default keycloak
