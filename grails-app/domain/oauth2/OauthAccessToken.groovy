package oauth2

class OauthAccessToken {

    String tokenId
    Byte[] token
  	String authenticationId
  	String userName
  	String clientId

  	Byte[] authentication
  	String refreshToken

    Date dataCricao = new Date()
    Date dataExpiracao


    static constraints = {
  		tokenId nullable:true,unique:true
  		token nullable:true
  		authenticationId nullable:true
  		userName nullable:true
  		clientId nullable:true
  		authentication nullable:true
  		refreshToken nullable:true
      dataCricao  nullable:true
      dataExpiracao  nullable:true

    }

	static mapping = {
		version false
	}
}
