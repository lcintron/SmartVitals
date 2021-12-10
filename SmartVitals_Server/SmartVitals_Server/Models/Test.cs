using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.ComponentModel.DataAnnotations;
using SmartVitals_Server.Models;

namespace SmartVitals.Services.Models
{
    public class Test
    {
        [Key]
        public int TestId { get; set; }
        public String UserName { get; set; }
        public ICollection<Sample> Samples { get; set; }
        public String Description { get; set; }
        public ICollection<Comment> Comments { get; set; }
        public double Latitude { get; set; }
        public double Longitude { get; set; }
        public DateTime Date { get; set; }
    }
}
