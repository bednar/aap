apiaryExpected = new File(basedir, "ApiaryExpected.md")
assert apiaryExpected.exists()

apiaryResult = new File(basedir, "target/generated/apiary/Apiary.md")
assert apiaryResult.exists()

assert apiaryExpected.getText().equals(apiaryResult.getText())