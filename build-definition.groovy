node {
 ws('/tmp/jobs') {

  // snippet to print environment variable
  sh 'env > env.txt'
  String[] envs = readFile('env.txt').split("\r?\n")
  for (String vars: envs) {
   println(vars)
  }
  def mvnHome
  stage('Preparation') {
   // Get some code from a GitHub repository
   git 'https://github.com/soumyakbhattacharyya/jenkins-pipeline.git'
    // Get the Maven tool.
    // ** NOTE: This 'MAVEN3.0.5' Maven tool must be configured
    // **       in the global configuration.
   mvnHome = tool 'MAVEN3.0.5'
   sh 'ls -lrt'
  }
  stage('Build') {
   // Run the maven build
   sh 'cd /tmp/jobs/graph-util'
   sh 'ls -lrt'
   if (isUnix()) {
    sh "'${mvnHome}/bin/mvn' -f /tmp/jobs/graph-util/pom.xml -Dmaven.test.failure.ignore clean package"
   } else {
    bat(/"${mvnHome}\bin\mvn" -Dmaven.test.failure.ignore clean package/)
   }
  }
  stage('Results') {
   junit '**/target/surefire-reports/TEST-*.xml'
   archive 'target/*.jar'
  }
 }
}