expected = """HOST: http://demoaap.apiary.io

--- Demo Application ---

"""
apiaryMd = new File(basedir, "Apiary.md")

assert apiaryMd.exists()
assert apiaryMd.getText().equals(expected)