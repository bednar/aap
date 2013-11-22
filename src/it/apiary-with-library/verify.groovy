expected = """HOST: http://demoaap.apiary.io

--- Demo Application ---

--
Security API
API for authentication and security interaction.
--


"""
apiaryMd = new File(basedir, "target/generated/apiary/Apiary.md")

assert apiaryMd.exists()
assert apiaryMd.getText().equals(expected)