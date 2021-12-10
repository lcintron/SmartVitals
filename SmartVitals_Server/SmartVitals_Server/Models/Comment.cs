using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.ComponentModel.DataAnnotations;
using SmartVitals.Services.Models;

namespace SmartVitals_Server.Models
{
    public class Comment
    {
        [Key]
        public int CommentId { get; set; }
        public int TestId { get; set; }
        //public Test Test { get; set; }
        public String Message {get;set;}
        public String UserName { get; set; }
        public DateTime Date { get; set; }
    }
}
