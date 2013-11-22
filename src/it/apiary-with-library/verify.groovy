expected = """HOST: http://demoaap.apiary.io

--- Demo Application ---

--
Library API
Testing api in library.
--


"""
apiaryMd = new File(basedir, "target/generated/apiary/Apiary.md")

assert apiaryMd.exists()
assert apiaryMd.getText().equals(expected)