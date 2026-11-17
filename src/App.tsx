/**
 * @license
 * SPDX-License-Identifier: Apache-2.0
 */

export default function App() {
  return (
    <div className="min-h-screen bg-[#f4f7f5] text-gray-800 py-10 px-4">
      <div className="max-w-3xl mx-auto bg-white border border-[#d1e7d8] border-t-4 border-t-[#166534] rounded-md p-6 sm:p-8 shadow-xs">
        <div className="border-b border-[#d1e7d8] pb-4 mb-6 flex flex-wrap items-center justify-between gap-4">
          <div>
            <h1 className="text-2xl font-bold text-[#14532d]">
              Suivi Lapins — Dossier Java / Maven
            </h1>
            <p className="text-sm text-gray-600 mt-1">
              Le projet Java 17 / Spring Boot 3 / PostgreSQL autonome est prêt dans le dossier <code className="font-mono">suivi-lapins/</code>
            </p>
          </div>
          <a
            href="/suivi-lapins.zip"
            download="suivi-lapins.zip"
            className="inline-block bg-[#166534] hover:bg-[#14532d] text-white font-semibold text-sm px-4 py-2.5 rounded"
          >
            Télécharger suivi-lapins.zip
          </a>
        </div>

        <div className="space-y-5 text-sm leading-relaxed">
          <section className="bg-[#f0fdf4] border border-[#bbf7d0] text-[#14532d] rounded p-4">
            <h2 className="font-semibold mb-1">
              Si vous connectez cet espace directement à GitHub :
            </h2>
            <p>
              Le dépôt GitHub contiendra le dossier <code className="font-mono bg-white px-1.5 py-0.5 rounded border border-[#bbf7d0]">suivi-lapins/</code>.
              Lors de la création du <strong>Web Service Docker</strong> sur <strong>Render</strong>, renseignez obligatoirement :
            </p>
            <ul className="list-disc pl-5 mt-2 space-y-1">
              <li><strong>Root Directory :</strong> <code className="font-mono bg-white px-1.5 py-0.5 rounded border border-[#bbf7d0]">suivi-lapins</code></li>
              <li><strong>Language / Runtime :</strong> <code className="font-mono bg-white px-1.5 py-0.5 rounded border border-[#bbf7d0]">Docker</code></li>
            </ul>
          </section>

          <section>
            <h2 className="text-base font-semibold text-[#14532d] mb-2">
              Ou si vous souhaitez envoyer uniquement le dossier sur GitHub / NetBeans :
            </h2>
            <ol className="list-decimal pl-5 space-y-1.5 text-gray-700">
              <li>Cliquez sur <strong>Télécharger suivi-lapins.zip</strong> ci-dessus et décompressez l&apos;archive.</li>
              <li>Ouvrez le dossier <code className="font-mono">suivi-lapins</code> dans Apache NetBeans ou poussez son contenu dans un dépôt GitHub dédié.</li>
              <li>Configurez PostgreSQL et les variables d&apos;environnement (<code className="font-mono">DB_URL</code>, <code className="font-mono">DB_USERNAME</code>, <code className="font-mono">DB_PASSWORD</code>, <code className="font-mono">DEMO_MODE</code>, <code className="font-mono">DEMO_PASSWORD</code>) selon <code className="font-mono">suivi-lapins/README.md</code>.</li>
            </ol>
          </section>
        </div>
      </div>
    </div>
  );
}
