package oauth2

class OauthClientDetails {

	String clientId
	String resourceIds
	String clientSecret
	String scope
	String authorizedGrantTypes
	String webServerRedirectUri
	String authorities
	Long accessTokenValidity
	Long refreshTokenValidity
	String additionalInformation

    static constraints = {

		clientId nullable:false,unique:true
		resourceIds nullable:true
		clientSecret nullable:true
		scope nullable:true
		authorizedGrantTypes nullable:true
		webServerRedirectUri nullable:true
		authorities nullable:true
		accessTokenValidity nullable:true
		refreshTokenValidity nullable:true
		additionalInformation nullable:true

    }
}
