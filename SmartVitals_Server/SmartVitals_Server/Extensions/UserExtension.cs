using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using SmartVitals_Server.Models;

namespace SmartVitals_Server.Extensions
{
    public  static class UserExtension{
        public static UserInfo Profile(this System.Security.Principal.IIdentity identity)
        {
            var profile = WebProfile.GetProfile(identity.Name);
            return UserInfo.GetFromProfile(profile);
        }
    }
}
