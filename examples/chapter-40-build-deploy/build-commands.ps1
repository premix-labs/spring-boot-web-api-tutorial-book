.\gradlew.bat clean build

$env:SPRING_PROFILES_ACTIVE = "prod"
$env:DB_URL = "jdbc:postgresql://localhost:5432/secure_admin"
$env:DB_USERNAME = "postgres"
$env:DB_PASSWORD = "admin123"
$env:JWT_SECRET = "0123456789012345678901234567890123456789012345678901234567890123"

java -jar build/libs/backend-api-0.0.1-SNAPSHOT.jar

