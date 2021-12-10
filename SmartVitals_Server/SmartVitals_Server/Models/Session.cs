using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.ComponentModel.DataAnnotations;

namespace SmartVitals_Server.Models
{
    public class Session 
    {
        [Key]
        public int SessionId { get; set; }
        public Guid SessionGuid { get; set; }
        public String DeviceId { get; set; }
        public bool IsValid { get; set; }
        public String UserName { get; set; }
        public DateTime OpenedOn { get; set; }
        public DateTime ClosedOn { get; set; }
    }
}
