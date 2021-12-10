using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Data.Entity;
using SmartVitals.Services.Models;

namespace SmartVitals_Server.Models
{
    public class SmartVitalsContext : DbContext
    {
        public SmartVitalsContext() : base("SmartVitalsContext") { }
        public DbSet<Session> Sessions { get; set; }
        public DbSet<Test> Tests { get; set; }
        public DbSet<Sample> Samples { get; set; }
        public DbSet<Comment> Comments { get; set; }
    }
}