using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using SmartVitals_Server.Models;
using SmartVitals.Services.Models;
using System.Collections;

namespace SmartVitals_Server.Context
{
    public class DbHelpers
    {
        private static SmartVitalsContext db = new SmartVitalsContext();

        public static IEnumerable<Session> GetSessions()
        {
            return db.Sessions.ToList();
        }

        public static bool AddSession(Session session)
        {
            try
            {
                if (session == null)
                    return false;
                db.Sessions.Add(session);
                db.SaveChanges();
                return true;
            }
            catch (Exception)
            {
                return false;
            }
        }

        public static bool RemoveSession(Session session)
        {
            try
            {
                db.Sessions.Remove(session);
                db.SaveChanges();
                return true;
            }
            catch (Exception)
            {
                return false;
            }
        }

        public static bool RemoveSession(String SessionGuid)
        {
            try
            {
                var session = GetSessionByGuid(SessionGuid);
                if (session != null)
                {
                    db.Sessions.Remove(session);
                    db.SaveChanges();
                }
                return true;
            }
            catch (Exception)
            {
                return false;
            }
        }

        public static bool RemoveSession(int SessionId)
        {
            try
            {
                var session = db.Sessions.Where(s=>s.SessionId==SessionId).FirstOrDefault();
                if (session != null)
                {
                    db.Sessions.Remove(session);
                    db.SaveChanges();
                }
                return true;
            }
            catch (Exception)
            {
                return false;
            }
        }

        public static Session GetSessionByGuid(String SessionGuid)
        {
            var guid = new Guid(SessionGuid);
            return db.Sessions.Where(s => s.SessionGuid == guid).FirstOrDefault();
        }

        public static Session GetSessionById(int SessionId)
        {
            return db.Sessions.Where(s => s.SessionId == SessionId).FirstOrDefault();
        }

        public static bool SessionIsValid(String SessionGuid)
        {
            var guid = new Guid(SessionGuid);
            return db.Sessions.Where(s => s.SessionGuid == guid && s.IsValid).Any();
        }

        public static bool AddTest(Test Test)
        {
            try
            {
                if (Test == null)
                    return false;
                Test.Description= Test.Description ?? String.Empty;
                db.Tests.Add(Test);
                db.SaveChanges();
                return true;
            }
            catch (Exception)
            {
                return false;
            }
        }

        public static Test GetTestById(int TestId, bool DeepLoad=false)
        {
            try
            {
                var test = db.Tests.Where(t => t.TestId == TestId).FirstOrDefault();
                if (test == null || !DeepLoad)
                    return test;
                else
                {
                    test.Samples = db.Samples.Where(s => s.TestId == test.TestId).ToList();
                    test.Comments = db.Comments.Where(c => c.TestId == test.TestId).ToList();
                }
                return test;
            }
            catch (Exception)
            {
                return null;
            }
        }

        public static IEnumerable<Test> GetTestsByUserName(String UserName)
        {
            return db.Tests.Where(test => test.UserName == UserName).ToList();
        }

    }
}