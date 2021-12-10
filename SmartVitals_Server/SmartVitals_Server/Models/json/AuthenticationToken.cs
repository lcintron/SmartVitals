using System;


namespace SmartVitals_Server.Models.json
{
    public class AuthenticationToken
    {
        public bool IsAuthenticated { get; set; }
        public string Message { get; set; }
        public string Role { get; set; }
        public UserInfo Profile { get; set; }
        public Guid SessionId { get; set; }
    }

}
