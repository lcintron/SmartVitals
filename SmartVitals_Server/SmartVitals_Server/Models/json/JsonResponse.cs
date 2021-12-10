using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace SmartVitals_Server.Models.json
{
    public class JsonResponse<T>
    {
        public T Response { get; set; }
        public bool Success { get;set; }
        public string Message { get; set; }
    }
}
