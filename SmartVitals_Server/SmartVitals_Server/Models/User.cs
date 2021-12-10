using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace SmartVitals_Server.Models
{
    public class User
    {
        public String UserName { get; set; }
        public String Email { get; set; }
        public String Roles { get; set; }
        public DateTime LastActivity { get; set; }
    }
}